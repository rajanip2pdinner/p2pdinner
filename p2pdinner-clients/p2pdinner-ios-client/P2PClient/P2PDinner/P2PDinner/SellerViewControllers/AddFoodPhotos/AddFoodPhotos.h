//
//  AddFoodPhotos.h
//  P2PDinner
//
//  Created by Selvam M on 3/29/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SelectPhotoCell.h"
#import "ItemDetails.h"

typedef void(^CompletionAction)(bool);

@interface AddFoodPhotos : UIViewController<SelectPhotoDelegate,UIActionSheetDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate>
{
    IBOutlet UITableView *photoSelectTableView;
    UIButton *photoButton;
    NSArray *imageURLArray;
    NSMutableArray *imageURLMutableArray;
    
}
@property(nonatomic,strong) ItemDetails *itemDetails;
@property(nonatomic,weak) IBOutlet UIButton *updateSaftyBtn;
@property(nonatomic,assign) BOOL isFromFoodSafty;
@property(nonatomic,assign) BOOL isFromSettings;
@property(nonatomic,strong) CompletionAction completionAction;
- (void)completionAction:(void (^ __nullable)(bool))completion;
- (IBAction)updateImageToProfile;
@end
