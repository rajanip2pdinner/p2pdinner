//
//  SettingsViewController.m
//  P2PDinner
//
//  Created by Selvam M on 5/31/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import "SettingsViewController.h"
#import "AgreementsViewController.h"
#import "StringConstants.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:kTitle_Settings];
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [self performSegueWithIdentifier:kSegueID_AgreementVC sender:indexPath];

}
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    AgreementsViewController *viewController=[segue destinationViewController];
    NSString *filePath;
    NSIndexPath *indexPath=sender;
    if (indexPath.row==0) {
       filePath = kTerms_URL;
        [viewController setTitle:kTermsConditionText];
    }
    else if (indexPath.row==1){
        filePath = kCopyright_URL;
        [viewController setTitle:kCopyrightText];
    }else {
        filePath = kPrivacy_URL;
        [viewController setTitle:kPrivacyText];
    }
     NSURL *targetURL = [NSURL URLWithString:filePath];
     [viewController setPdfURL:targetURL];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
